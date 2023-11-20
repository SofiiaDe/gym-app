export function getCookie(name: string) {
  const value = `; ${document.cookie}`
  const parts = value.split(`; ${name}=`)
  if (parts?.length === 2) return parts.pop()?.split(";").shift()
}

export function getAuthHeader() {
  return {
    authorization: `Bearer ${getAccessToken()}`,
  }
}

export const getAccessToken = () => getCookie("accessToken")

export function isAuthorized() {
  return !!getCookie("accessToken") && isTokenActive()
}

export function parseJwt(token: string) {
  // var base64Url = token.split('.')[1]
  // var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
  // var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
  //     return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
  // }).join(''))

  // return JSON.parse(jsonPayload)

  return {
    exp: 163512351222,
  }
}

export function isTokenActive() {
  const token = getAccessToken()

  if (!token) return false

  return parseJwt(token)?.exp * 1000 > Date.now()
}

export function deleteAuthCookies() {
  deleteCookieByName(ACCESS_TOKEN)
  deleteCookieByName(REFRESH_TOKEN)
  deleteCookieByName(USER_NAME)
  deleteCookieByName(USER_TYPE)
}

function deleteCookieByName(name: string) {
  document.cookie = name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;"
}

export function setAuthCookies(accessToken: string, refreshToken: string) {
  setCookieByName(ACCESS_TOKEN, accessToken)
  setCookieByName(REFRESH_TOKEN, refreshToken)
}

export function setCookieByName(name: string, value: string) {
  document.cookie = `${name}=${value}; Path=/`
}

export function getUsernameCookie() {
  return getCookie(USER_NAME)
}

export function setUsernameCookie(name: string) {
  setCookieByName(USER_NAME, name)
}

export function getUserTypeCookie() {
  return getCookie(USER_TYPE)
}

export function formatDate(date: Date): string {
  const year = date.getFullYear().toString().padStart(4, "0")
  const month = (date.getMonth() + 1).toString().padStart(2, "0")
  const day = date.getDate().toString().padStart(2, "0")
  return `${year}-${month}-${day}`
}

const ACCESS_TOKEN = "accessToken"
const REFRESH_TOKEN = "refreshToken"
const USER_NAME = "username"
const USER_TYPE = "userType"
